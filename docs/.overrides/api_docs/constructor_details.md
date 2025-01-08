{% if page.meta.constructors %}
## Constructor Details

{% for constructor in page.meta.constructors %}
<div class="api-detail" markdown>

{% set name = namespace(text = constructor.name ~ "(") %}
{% for parameter in constructor.parameters %}
  {% set name.text = name.text ~ parameter.type %}
  {% if loop.revindex > 1 %}{% set name.text = name.text ~ ", " %}{% endif %}
{% endfor %}
{% set name.text = name.text ~ ")" %}

{% if constructor.deprecated %}
  {% set name.text = "<api__deprecated></api_deprecated> " ~ name.text %}
{% endif %}

{% for attribute in constructor.attributes %}
  {% set name.text = "<api__{{- attribute -}}></api__{{- attribute -}}> " ~ name.text %}
{% endfor %}

### {{ name.text }} { #{{ name.text | lower() | replace(' ', '-') | e }} }

{% if constructor.deprecated %}
/// deprecated | Deprecation Warning
{{ constructor.deprecated }}
///
{% endif %}

{% if constructor.description %}
<p class="api-detail__description">

{{ constructor.description }}

</p>
{% endif %}

{% if constructor.parameters %}
#### Parameters: { #{{ constructor.name | lower() | replace(' ', '-') | e }}-parameters }

{% for parameter in constructor.parameters %}
- {% for attribute in parameter.attributes %}<api__{{- attribute -}}></api__{{- attribute -}}>{% endfor %} `{{ parameter.type | e}}: {{ parameter.name }}`{% if parameter.description %} - {{ parameter.description }}{% endif %}
{% endfor %}
{% endif %}

</div>
{% endfor %}

{% endif %}