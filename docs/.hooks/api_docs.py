def on_pre_page_macros(env):
    footer = "\n\n{% if page.meta and page.meta.api %}{% include 'api_docs/layout.md' %}{% endif %}"
    env.markdown += footer