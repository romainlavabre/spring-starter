[BACK](../table.md)

---
 
# Encoder Json

The json encoder permit to manage the output for object,
you can apply easier the filters, or ignored property etc.

### Configure your object

#### Default
By default, your configuration is

```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group
    })
    private int id;
}
```

For this case, it will produce this output:

```json
{
  "id": integer
}
```


#### Rename key


```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(key = "custom_id")
    })
    private int id;
}
```

output:
```json
{
  "custom_id": integer
}
```

#### Apply Formatter

For know how build a custom formatter, see the section "Build Formatter"
 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(formatter = ToUpperFormatter.class)
    })
    private String name;
}
```

output:
```json
{
  "name": "UPPER CASE NAME"
}
```


#### Apply Overwrite

For know how build a custom overwrite, see the section "Build Overwrite"
 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(overwrite = RenameToClientName.class)
    })
    private String name;
}
```

output:
```json
{
  "name": "company name replaced by client name"
}
```

#### Specify object


The encoder need know when take, or travel the object 
 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(object = true)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "sample1": Id of ressource
}
```

#### Encode object

 
```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group(object = true, onlyId = false)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "sample1": {
     "properties": values
  }
}
```

#### Merge ascent object


```java
import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group
    })
    private String name;

    @Json(groups = {
        @Group(object = true, onlyId = false, ascent = true)
    })
    private Sample1 sample1;
}
```

output:
```json
{
  "name": "name",
  "properties of sample 1": values
}
```


#### Multiple configurations

```java
import com.replace.replace.api.json.GroupType;import com.replace.replace.api.json.annotation.Group;
import com.replace.replace.api.json.annotation.Json;

public class Sample{

    @Json(groups = {
        @Group,
        @Group(name = GroupType.GROUP_NAME)
    })
    private String name;

    @Json(groups = {
        @Group(object = true, onlyId = false, ascent = true),
        @Group(object = true, key = "sample1_id")
    })
    private Sample1 sample1;
}
```

### Encode your data

```java
import com.replace.replace.api.json.Encoder;import com.replace.replace.api.json.GroupType;import java.util.Map;

public class SampleController {

    public Map<String, Object> getSample(Sample sample){
        
        return Encoder.encode( sample, GroupType.DEFAULT );
    }
}
```


---

[BACK](../table.md)