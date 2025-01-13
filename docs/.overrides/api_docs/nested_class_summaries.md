{% if page.meta.classes %}
## Nested Class Summary

<table>
  <thead>
    <tr>
      <th>Modifier and Type</th>
      <th>Class</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    {% for class in page.meta.classes %}
    <tr>
      {% set modifiers = namespace(values = "") %}
      {% for attribute in class.attributes %}
        {% set modifiers.values = modifiers.values ~ attribute %}
        {% if loop.revindex > 1 %}{% set modifiers.values = modifiers.values ~ " " %}{% endif %}
      {% endfor %}
      {% if modifiers.values %}
        {% set modifiers.values = modifiers.values ~ " " %}
      {% endif %}
      {% set modifiers.values = modifiers.values ~ class.type %}
      <td><code>{{ modifiers.values }}</code></td>
      <td><a href="{{ class.link }}"><code>{{ class.name }}</code></a></td>
      <td>{{ class.description.split("<br>")[0] }}</td>
    </tr>
    {% endfor %}
  </tbody>
</table>
{% endif %}