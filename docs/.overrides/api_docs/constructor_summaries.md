{% if page.meta.constructors %}
## Constructor Summary

| Constructor | Description |
|-------------|-------------|{% for constructor in page.meta.constructors %}
| [`{{ constructor.name }}({% for parameter in constructor.parameters -%}
{{ parameter.type }}{% if loop.revindex > 1 %}, {% endif %}
{%- endfor %})`](#{{ constructor.name | lower() }}) | {% if constructor.deprecated -%}
<api__deprecated></api__deprecated> {{ constructor.deprecated.split("\n")[0] }}
{%- elif constructor.description -%}
{{ constructor.description.split("\n")[0] }}
{%- endif %} |
{%- endfor %}
{% endif %}