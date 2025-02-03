{% macro render_name(method) -%}
{{ method.name }}({% for parameter in method.parameters %}{{ parameter.type }}{% if loop.revindex > 1 %}, {% endif %}{% endfor %})
{%- endmacro %}