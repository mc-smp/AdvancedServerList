import re

from mkdocs.config.defaults import MkDocsConfig
from mkdocs.structure.files import Files
from mkdocs.structure.pages import Page
from re import Match

def on_page_markdown(
    markdown: str, *, page: Page, config: MkDocsConfig, files: Files
):
    def replace(match: Match):
        platform = match.group(1)
        if platform == "paper":      return "[:fontawesome-solid-paper-plane:](https://papermc.io){ .md-button .md-button--icon title=\"PaperMC\" target=\"_blank\" rel=\"noopener\" }"
        elif platform == "bungee":   return "[:simple-spigotmc:](https://spigotmc.org){ .md-button .md-button--icon title=\"BungeeCord\" target=\"_blank\" rel=\"noopener\" }"
        elif platform == "velocity": return "[:simple-velocity:](https://velocitypowered.com){ .md-button .md-button--icon title=\"Velocity\" target=\"_blank\" rel=\"noopener\" }"
        elif platform == "proxies":  return "[:simple-spigotmc:](https://spigotmc.org){ .md-button .md-button--icon title=\"BungeeCord\" target=\"_blank\" rel=\"noopener\" } [:simple-velocity:](https://velocitypowered.com){ .md-button .md-button--icon title=\"Velocity\" target=\"_blank\" rel=\"noopener\" }"
        elif platform == "all":      return "[:fontawesome-solid-paper-plane:](https://papermc.io){ .md-button .md-button--icon title=\"PaperMC\" target=\"_blank\" rel=\"noopener\" } [:simple-spigotmc:](https://spigotmc.org){ .md-button .md-button--icon title=\"BungeeCord\" target=\"_blank\" rel=\"noopener\" } [:simple-velocity:](https://velocitypowered.com){ .md-button .md-button--icon title=\"Velocity\" target=\"_blank\" rel=\"noopener\" }"
        else:                        return platform
    
    return re.sub(
        r"<!-- icon:(paper|bungee|velocity|proxies|all) -->",
        replace, markdown, flags = re.I | re.M
    )