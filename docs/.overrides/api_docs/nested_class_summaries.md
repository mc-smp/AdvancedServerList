{% if page.meta.classes %}
## Nested Class Summary

| Modifier and Type | Class | Description |
|-------------------|-------|-------------|{% for class in page.meta.classes %}
| `{% if class.attributes -%}
{% for attribute in class.attributes -%}
{{ attribute }}{% if loop.revindex > 1 %} {% endif %}
{%- endfor %} {%- endif %}{{ class.type }}` | [`{{ class.name }}`]({{ class.link }}) | {{ class.description.split("\n")[0] }} |
{%- endfor %}
{% endif %}