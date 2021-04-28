[BACK](../table.md)

---

# Upload

The upload component permit to upload received document,
he is loaded by request.

### Configure a system

By default, you have this configuration:

```java
import com.replace.replace.api.upload.annotation.Move;
import com.replace.replace.api.upload.annotation.Duplication;
import com.replace.replace.api.upload.annotation.AcceptType;
import com.replace.replace.api.upload.annotation.Size;
import com.replace.replace.api.upload.annotation.TransactionSynchronized;


public interface UploadHandler extends EventSubscriber {

    
    @Move( moveRule = MoveS3.class )
    @Duplication( duplicationRule = DuplicationS3.class )
    @AcceptType( types = {
            ContentTypeResolver.APPLICATION_PDF,
            ContentTypeResolver.IMAGE_PNG,
            ContentTypeResolver.IMAGE_JPEG
    } )
    @Size( size = 1250000 )
    @TransactionSynchronized
    String DOCUMENT = "document";

}
``` 

- Call the MoveS3 for definitive upload
- Call the DuplicationS3 for manage the risky duplication
- Accept type:
    - application/pdf
    - image/jpg
    - image/png
- Max size is 1250000 bytes (10 MegaBits)
- The file will be uploaded when transaction will be succeeded

##### Exception

You can accept all types:

```java
import com.replace.replace.api.upload.annotation.AcceptType;
@AcceptType(all = true);
```

### Move rule

The move rule must implement:

```java
package com.replace.replace.api.upload.Move.MoveRule;
```

### Duplication rule

The duplication rule must implement:

```java
package com.replace.replace.api.upload.Duplication.DuplicationRule;
```

---

[BACK](../table.md)