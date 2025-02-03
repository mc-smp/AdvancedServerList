{% if page.meta.methods %}
## Method Details

{% for method in page.meta.methods %}
<div class="api-detail" markdown>

{% set modifiers = namespace(text = "") %}

{% if method.deprecated %}
  {% set modifiers.text = "<api__deprecated></api__deprecated> " %}
{% endif %}

{% for attribute in method.attributes %}
  {% set modifiers.text = modifiers.text ~ "<api__" ~ attribute ~ "></api__" ~ attribute ~ ">" %}
  {% if loop.revindex > 0 %}{% set modifiers.text = modifiers.text ~ " " %}{% endif %}
{% endfor %}

{% if modifiers.text %}{% set modifiers.text = modifiers.text ~ " " %}{% endif %}

{% set method_type = "" %}
{% if method.type %}
  {% if method.type.link %}
    {% set method_type = '[' ~ method.type.name|default('void')|e ~ '](' ~ method.type.link ~ '){ .api-type__' ~ method.type.type|default('primitive') ~ '} ' %}
  {% else %}
    {% set method_type = '<span class="api-type__' ~ method.type.type|default('primitive') ~ '">' ~ method.type.name|default('void')|e ~ '</span> ' %}
  {% endif %}
{% elif page.meta.defaults and page.meta.defaults.type %}
  {% if page.meta.defaults.type.link %}
    {% set method_type = '[' ~ page.meta.defaults.type.name|default('void')|e ~ '](' ~ page.meta.defaults.type.link ~ '){ .api-type__' ~ page.meta.defaults.type.type|default('primitive') ~ '} ' %}
  {% else %}
    {% set method_type = '<span class="api-type__' ~ page.meta.defaults.type.type|default('primitive') ~ '">' ~ page.meta.defaults.type.name|default('void')|e ~ '</span> ' %}
  {% endif %}
{% else %}
  {% set method_type = '<span class="api-type__primitive">void</span> ' %}
{% endif %}

{% set name = namespace(text = method.name ~ "(") %}
{% for parameter in method.parameters %}
  {% set name.text = name.text ~ parameter.type %}
  {% if loop.revindex > 1 %}{% set name.text = name.text ~ ", " %}{% endif %}
{% endfor %}
{% set name.text = name.text ~ ")" %}

### {{ modifiers.text }} {{ method_type }} `{{ name.text }}` { #{{ method.name | lower() | replace(' ', '-') | e }} }

{% if method.deprecated %}
/// deprecated | Deprecation Warning
{{ method.deprecated }}
///
{% endif %}

{% if method.description %}
<div class="api-detail__description" markdown>

{{ method.description }}

</div>
{% endif %}

{% if method.parameters %}
#### Parameters: { #{{ method.name | lower() | replace(' ', '-') | e }}-parameters }

<div class="api-detail__description" markdown>

{% for parameter in method.parameters %}
- {% for attribute in parameter.attributes %}<api__{{- attribute -}}></api__{{- attribute -}}>{% endfor %} `{{ parameter.type | e}}: {{ parameter.name }}`{% if parameter.description %} - {{ parameter.description }}{% endif %}
{% endfor %}

</div>
{% endif %}

{% if method.returns %}
#### Returns: { #{{ method.name | lower() | replace(' ', '-') | e }}-returns }

<div class="api-detail__description" markdown>

{{ method.returns }}

</div>
{% endif %}

{% if method.throws %}
#### Throws: { #{{ method.name | lower() | replace(' ', '-') | e }}-throws }

<div class="api-detail__description" markdown>

{% for throw in method.throws %}
- `{{ throw.name }}`{% if throw.description %} - {{ throw.description }}{% endif %}
{% endfor %}

</div>
{% endif %}

{% if method.seealso %}
#### See also: { #{{ method.name | lower() | replace(' ', '-') | e }}-see-also }

<div class="api-detail__description" markdown>

{% for see in method.seealso %}
- [`{{ see.name }}`]({{ see.link }})
{% endfor %}

</div>
{% endif %}

</div>
{% endfor %}

{% endif %}