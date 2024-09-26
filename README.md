# Simple Resource Loader
Simple Resource Loader is a tiny mod allowing you to add required (or optional) resource packs and data packs to your Minecraft instance, useful for any modpack or server maker.
It works on clients, singleplayer and dedicated servers, without a single config file. 
You just need to put packs (which can be either a folder or a zip file) in correct place.

## Usage
When started, the mod will create all required folders. Through you can also create them by hand if needed.
Folder follow a simple structure: `resources/<TYPE>/<REQUIREMENT>`. The `<TYPE>` can be replaced with `resourcepack` which loads as client side resource pack,
`datapack` which load as server/singleplayer data pack and `common` which is added to both (useful for when data pack and resource pack are both included in a single file).
The `<REQUIREMENT>` is replaced with `required` for packs that are always loaded and `optional` for ones which you can enable/disable at any time.

For example, if you put a pack in `resources/common/required/`, it will always load as both resource pack and data pack, but `resources/resourcepack/optional/`
will add it as only as toggleable resource pack.
