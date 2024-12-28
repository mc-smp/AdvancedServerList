{% if page.meta.constructors %}
## Constructor Summary

| Constructor | Description |
|-------------|-------------|
{% for constructor in page.meta.constructors %}
{% set name = namespace(text = constructor.name ~ "(") %}
{% for parameter in constructor.parameters %}
  {% set name.text = name.text ~ parameter.type %}
  {% if loop.revindex > 0 %}{% set name.text = name.text ~ ", " %}{% endif %}
{% endfor %}
{% set name.text = name.text ~ ")" %}
{% set description = "" %}
{% if constructor.deprecated %}
  {% set description = "<api__deprecated></api__deprecated>" ~ constructor.deprecated.split("<br>")[0] %}
{% elif constructor.description %}
  {% set description = constructor.description.split("<br>")[0] %}
{% endif %}
| [`{{ name.text | e }}`](#{{ name.text | lower() | replace(' ', '-') | e }}) | {{ description }} |
{% endfor %}
{% endif %}