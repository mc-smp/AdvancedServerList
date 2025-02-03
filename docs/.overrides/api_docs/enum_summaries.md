{% if page.meta.enums %}
## Enum Constant Summary

| Enum | Description |
|------|-------------|{% for enum in page.meta.enums %}
| [`{{ enum.name | upper() }}`](#{{ enum.name | lower() }}) | {{ enum.description.split("\n")[0] }} |
{%- endfor %}
{% endif %}