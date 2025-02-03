def on_pre_page_macros(env):
    footer = """
    
    {% if page.meta and page.meta.api %}
    {% include 'api_docs/constructor_summaries.md' %}
    {% include 'api_docs/nested_class_summaries.md' %}
    {% include 'api_docs/enum_summaries.md' %}
    {% include 'api_docs/method_summaries.md' %}
    
    {% include 'api_docs/constructor_details.md' %}
    {% include 'api_docs/enum_details.md' %}
    {% include 'api_docs/method_details.md' %}
    {% endif %}
    """
    env.markdown += footer