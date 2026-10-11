# BBL Miners – Plan v2 (26.1.2, NeoForge)

## 0. Decisions so far
- **Power**: a fuel slot burns vanilla-burnable items into an internal energy buffer. The buffer is a real energy handler, so external FE input can be added later.
- **Mining is simulated and infinite**: the structure blocks are never touched. Each cycle rolls the loot table of a configured block, as Structure Loot already does.
- **Upgrades are "caps"**: structures define named cap positions. Cap blocks/items placed there give speed / efficiency / fortune and so on.
- **Structures are data-driven** so modpacks can add their own.
- **First structure**: a 3x3x3 of cobblestone with the Miner Controller on top. Coal fuels it.

## 1. What already exists on this PC (reuse, don't reinvent)

| Need | Existing code | Notes |
|---|---|---|
| Simulated block loot | `multiloader/Structure Loot` → `StructureLootBlockEntity.executeLootRoll` / `buildLootParams` (case `BLOCK`) | Builds `LootParams` with `BLOCK_STATE`, `TOOL`, `ORIGIN`, luck, then `lootTable.getRandomItems`. Our fortune/efficiency caps become enchantments on a virtual pickaxe passed as `TOOL`. |
| Data-driven definition + JEI + KubeJS | `StructureLootRecipe` (record with codec, stream codec, `Recipe` boilerplate, `LootRoll`), `LootGeneratorCategory`, `ClientRecipeCache`, Schema JS `LootGeneratorRecipeJS` | The pattern to copy for the structure definition. |
| Energy | `structureloot/util/EnergyHandler` extends `SimpleEnergyHandler`, `consumeEnergy` via `Transaction` | Copy it. `ContainerData` exposes amount/capacity to the GUI. |
| Rifts energy | `Chronopolis Companion` (`RiftsCapabilities.RIFT_ENERGY`) and `BBL Routers` rifts integration | Later: accept Rift energy as an extra power source. |
| Block entity/handler base | BBL Core: `SyncableBlockEntity`, `SyncableItemHandler` (per-slot insert/extract predicates, `runInternal`), `FakePlayerUtil` | Fuel slot and output slots. |
| 3x3x3 controller + part blocks | Structure Loot: `StructureLootBlock` (invisible, fills 3x3x3 with `PartBlock`s) | **Different approach**: it places parts itself. Ours *validates* player-built blocks, so we need new code. |
| Multiblock detection | BBL Core `CoreMultiblockDetector` | Scans hollow rectangular boxes. It does not fit fixed per-position components, so write a new matcher (and maybe move it to Core later). |
| Fuel to power | `Casting` has a `FuelRecipe` | Not needed, since we use vanilla burn time. |

## 2. Design

### 2.1 Blocks / items
- **Miner Controller** block + block entity (extends `SyncableBlockEntity`, `MenuProvider`, ticker).
- **Cap blocks / items**: Speed Cap, Efficiency Cap, Fortune Cap and so on. Tiered later.
- No special casing blocks in v1: the 3x3x3 is plain `minecraft:cobblestone`, matched from data.

### 2.2 Structure definition (data): pattern + keys
Same idea as `ShapedRecipePattern`: the pattern *is* the shape, and the keys say what each character means. Block matching uses vanilla's `ExtraCodecs.TAG_OR_ELEMENT_ID` (checked in the 26.1.2 sources: it parses `"minecraft:cobblestone"` as a block and `"#c:stone"` as a tag, giving `TagOrElementLocation(id, tag)`).

```json
{
  "type": "bblminers:structure",
  "structure_id": "bblminers:cobble_miner",
  "pattern": [
    ["CCC",
     "CCC",
     "CCC"],

    ["CCC",
     "CCC",
     "CCC"],

    ["CCC",
     "CCC",
     "CCC"],

    ["   ",
     " @ ",
     "   "]
  ],
  "keys": {
    "C": "minecraft:cobblestone"
  },
  "caps": [
    { "pos": [1, 0, 0], "type": "speed" },
    { "pos": [-1, 0, 0], "type": "fortune" }
  ],
  "cap_limits": { "speed": 4, "fortune": 3 },
  "mines": [
    { "block": "minecraft:cobblestone", "loot_table": "minecraft:blocks/cobblestone", "weight": 1 }
  ],
  "base_duration": 100,
  "base_rolls": 1,
  "energy_per_tick": 20
}
```
- **Layers go bottom to top** (`pattern[y]`), each layer is a list of rows (`z`, north to south), each row is a string (`x`, west to east). Your sketch used nested arrays. Strings are far easier for pack makers to read and edit, and match vanilla's shaped recipes.
- **Reserved characters**: `@` is the controller (exactly one, it is the origin). A space means "don't care" (anything, including air). Every other character must appear in `keys`.
- **Keys** map one character to a `TagOrElementLocation`. Optional extension: a key may also be a list of them (any of these) or `{ "block": "...", "optional": true }`.
- **Caps** use `pos` relative to the controller `@`, as you wrote them. Cap positions must be empty in the pattern (a space). A load-time check rejects overlaps.
- **Load-time validation** (like `ShapedRecipePattern`): rows are the same length, layers have the same row count, exactly one `@`, no undefined characters, every key is a single character. The codec returns a clear error and the structure is skipped.
- **Compiled form**: the pattern is turned into a list of `(offset from @, matcher)` entries once on load. Matching and the JEI/ghost preview both use that list. Nothing parses strings at runtime.
- **Matching a key**: for an element, `state.is(block)`. For a tag, `state.is(TagKey.create(Registries.BLOCK, id))`. Tags are synced to the client, so previews work there too.
- **Rotation**: the matcher can try the 4 horizontal rotations of the compiled list. Skipped for v1, since the cobble cube is symmetric.
- **Tiering later**: structures chained with `upgrades_from`. The controller picks the highest valid one.
- `type` is the recipe serializer id. This still follows the Structure Loot recipe pattern, so JEI, sync and KubeJS come with it. The only new code is the pattern codec.
### 2.3 Cap stats (data)
Block datamap `bblminers:miner_cap` (the Neo Vitae `altar_rune_stats` idea), or a recipe type like `bblminers:miner_cap` if you prefer all-recipes:
```json
{ "bblminers:speed_cap_1":   { "type": "speed",   "value": 0.25 },
  "bblminers:fortune_cap_1": { "type": "fortune", "value": 1 } }
```
How types apply:
- **speed / efficiency**: `duration = base_duration / (1 + Σ speed)`, and may also cut energy cost.
- **fortune**: virtual pickaxe with `Fortune N` enchantment in the loot `TOOL`.
- **silk touch** (later): virtual pickaxe with `Silk Touch`.
- Caps per type are limited by `cap_limits` and by how many cap positions the structure has.

### 2.4 Controller behaviour
1. **Validate** when placed, when a block changes inside the structure bounds, or on right-click. A slow fallback every ~100 ticks is fine. Cache: matched structure, cap totals, effective duration. Never scan every tick.
2. **Fuel**: one fuel slot. When the energy buffer has room, burn one item: `energy += burnTicks * fuelRatio` (config, so 1 coal = 1600 burn ticks → N energy). Container items (e.g. lava bucket) leave their remainder. Energy bar shows amount/capacity in the GUI.
3. **Tick** (server only): if valid, not redstone-paused, output has room and energy ≥ `energy_per_tick`: consume and `progress++`. At `duration`, roll the loot (as `executeLootRoll`) and insert into output slots. Wait if full.
4. **Output**: internal output slots (e.g. 9 to start) exposed through the item capability, and a push to an adjacent inventory (later, optional).
5. **Capabilities**: item handler (fuel in, output out), energy handler (insert later, once you want FE input).
6. **Feedback**: GUI shows "Structure valid / invalid, first mismatch at (x,y,z) expects …".
7. Persist inventory, energy, progress with `ValueOutput`/`ValueInput`, as your other block entities do.

### 2.5 Client / JEI
- JEI category "Miner Structure": layered 3D preview from `components`, cap positions highlighted, mined blocks and the drop list. Copy `LootGeneratorCategory` and `LootPreviewPacket` from Structure Loot.
- A ghost-block preview item can come after v1.

## 3. Phases
1. **Skeleton**: registries (block, item, BE type, menu), `Miners` wiring, controller block opens an empty GUI.
2. **Structure data**: `MinerStructureRecipe` (codecs, region shorthand), recipe-type registration, datagen of `cobble_miner`, client sync.
3. **Matcher**: `StructureMatcher` handling components, tags, optional, rotations; `/bblminers validate` debug output. Unit-test with the 3x3x3.
4. **Fuel and energy**: fuel slot, burn-to-energy, energy bar, config ratio.
5. **Mining loop**: progress, loot roll with block context, output slots, redstone pause. At this point the cobble miner works end to end.
6. **Caps**: cap blocks, datamap, speed/fortune/efficiency application, cap-position validation.
7. **Tiers + more structures**: `upgrades_from`, a second structure (e.g. stone/ore) to prove the data model.
8. **UX**: JEI category, GUI error hints, particles/sounds, Rifts energy input, example datapack for pack makers.

## 4. Open questions
1. **"Cap system like I did before"**: I could not find the old code. My reading is that a cap is a block placed at a structure-defined position, and its type gives the bonus. If the old system worked differently (e.g. cap *items* in GUI slots, or caps that stack on the controller), tell me and I will adjust 2.2/2.3.
2. **Fuel conversion rate**: how much energy per burn tick, and the buffer size? (E.g. coal = 1600 ticks × 10 = 16,000 energy.)
3. **Structure as recipe vs. datapack registry**: recipe type recommended for consistency with Structure Loot and Schema JS.
4. **Does the controller sit on top of the cobble, with the caps above or beside it?** (The example assumes caps on the layer above the cobble, around the controller.)

## 5. Risks
- 26.1.2 APIs (`ResourceHandler`, `Transaction`, `ValueInput/Output`, fuel values) differ from older versions. Copy the working patterns from Structure Loot and BBL Utilities, and check `fuelValues` / burn-time lookup against the current NeoForge source.
- Recipe-based definitions need the client copy for JEI and previews. Structure Loot's `ClientRecipeCache` already solves this.
- Loot-table simulation with a fake `TOOL` stack: keep the virtual pickaxe cached rather than rebuilt each roll.

