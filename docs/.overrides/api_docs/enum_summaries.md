{% if page.meta.enums %}
## Enum Constant Summary

<table>
  <thead>
    <tr>
      <th>Enum</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    {% for enum in page.meta.enums %}
    <tr>
      <td><a href="#{{ enum.name | lower() }}"><code>{{ enum.name | upper() }}</code></a></td>
      <td>{{ enum.description.split("<br>")[0] }}</td>
    </tr>
    {% endfor %}
  </tbody>
</table>
{% endif %}