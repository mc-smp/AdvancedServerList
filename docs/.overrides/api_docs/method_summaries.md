{% if page.meta.methods %}
## Method Summary

<table>
  <thead>
    <tr>
      <th>Modifier and Type</th>
      <th>Method</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    {% for method in page.meta.methods %}
    <tr>
      {% set modifiers = namespace(text = "") %}
      {% for attribute in method.attributes %}
        {% set modifiers.text = modifiers.text ~ attribute %}
        {% if loop.revindex > 0 %}{% set modifiers.text = modifiers.text ~ " " %}{% endif %}
      {% endfor %}
      
      {% if modifiers.text %}{% set modifiers.text = modifiers.text ~ " " %}{% endif %}
      
      {% if method.type and method.type.name %}
        {% set modifiers.text = modifiers.text ~ method.type.name %}
      {% elif page.meta.defaults and page.meta.defaults.type and page.meta.defaults.type.name %}
        {% set modifiers.text = modifiers.text ~ page.meta.defaults.type.name %}
      {% else %}
        {% set modifiers.text = modifiers.text ~ "void" %}
      {% endif %}
      
      {% set name = namespace(text = method.name ~ "(") %}
      {% for parameter in method.parameters %}
        {% set name.text = name.text ~ parameter.type %}
        {% if loop.revindex > 1 %}{% set name.text = name.text ~ ", " %}{% endif %}
      {% endfor %}
      {% set name.text = name.text ~ ")" %}
      
      {% set description = "" %}
      {% if method.deprecated %}
        {% set description = "<api__deprecated></api__deprecated>" ~ method.deprecated.split("<br>")[0] %}
      {% elif method.description %}
        {% set description = method.description.split("<br>")[0] %}
      {% endif %}
      {% if modifiers.text %}
        <td><code>{{ modifiers.text }}</code></td>
      {% else %}
        <td></td>
      {% endif %}
      <td><a href="#{{ name.text | lower() | replace(' ', '-') | e }}"><code>{{ name.text | e }}</code></a></td>
      <td>{{ description }}</td>
    </tr>
    {% endfor %}
  </tbody>
</table>
{% endif %}