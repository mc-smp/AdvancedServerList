{% if page.meta.methods or page.meta.inherits %}
## Method Summary

{% if page.meta.methods %}
| Modifier and Type | Method | Description |
|-------------------|--------|-------------|{% for method in page.meta.methods %}
| `{% if method.attributes -%}
{% for attribute in method.attributes -%}
{{ attribute }}{% if loop.revindex > 0 %} {% endif %}
{%- endfor %} {%- endif %}{% if method.type and method.type.name -%}
{{ method.type.name }}
{%- elif page.meta.defaults and page.meta.defaults.type and page.meta.defaults.type.name -%}
{{ page.meta.defaults.type.name }}
{%- else -%}void{%- endif %}` | [`{{ method.name }}({% for parameter in method.parameters -%}
{{ parameter.type }}{% if loop.revindex > 1 %}, {% endif %}
{%- endfor %})`](#{{ method.name | lower() }}) | {% if method.deprecated -%}
<api__deprecated></api__deprecated> {{ method.deprecated.split("\n")[0] }}
{%- elif method.description -%}
{{ method.description.split("\n")[0] }}
{%- endif %} |
{%- endfor %}
{% endif %}

{% if page.meta.inherits %}
{% for classpath, inherit in page.meta.inherits.items() %}
| Methods inherited from [`{{ classpath }}`]({{ inherit.link }}) |
|----------------------------------------------------------------|
| {% for entry in inherit.list -%}[`{{ entry.name | e }}`]({{ inherit.link }}#{{ entry.link | lower() | replace(' ', '-') | e }}){% if loop.revindex > 1 %}, {% endif %}{% endfor %} |
{% endfor %}
{% endif %}
{% endif %}