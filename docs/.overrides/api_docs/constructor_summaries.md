{% if page.meta.constructors %}
## Constructor Summary

<table>
  <thead>
    <tr>
      <th>Constructor</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    {% for constructor in page.meta.constructors %}
    <tr>
      {% set name = namespace(text = constructor.name ~ "(") %}
      {% for parameter in constructor.parameters %}
        {% set name.text = name.text ~ parameter.type %}
        {% if loop.revindex > 1 %}{% set name.text = name.text ~ ", " %}{% endif %}
      {% endfor %}
      {% set name.text = name.text ~ ")" %}
      {% set description = "" %}
      {% if constructor.deprecated %}
        {% set description = "<api__deprecated></api__deprecated>" ~ constructor.deprecated.split("<br>")[0] %}
      {% elif constructor.description %}
        {% set description = constructor.description.split("<br>")[0] %}
      {% endif %}
      <td><a href="#{{ name.text | lower() | replace(' ', '-') | e }}"><code>{{ name.text | e }}</code></a></td>
      <td>{{ description }}</td>
    </tr>
    {% endfor %}
  </tbody>
</table>
{% endif %}