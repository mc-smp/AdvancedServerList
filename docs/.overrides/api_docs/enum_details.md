{% if page.meta.enums %}
## Enum Constant Details

{% for enum in page.meta.enums %}
<div class="api-detail" markdown>

{% set modifiers = namespace(text = "<api__static></api__static> <api__final></api__final> ") %}

{% if enum.deprecated %}
  {% set modifiers.text = "<api__deprecated></api__deprecated> " ~ modifiers.text %}
{% endif %}

### {{ modifiers.text }} `{{ enum.name | upper() }}` { #{{ enum.name | lower() | replace(' ', '-') | e }} }

{% if enum.deprecated %}
/// deprecated | Deprecation Warning
{{ enum.deprecated }}
///
{% endif %}

{% if enum.description %}
<p class="api-detail__description">

{{ enum.description }}

</p>
{% endif %}

</div>
{% endfor %}

{% endif %}