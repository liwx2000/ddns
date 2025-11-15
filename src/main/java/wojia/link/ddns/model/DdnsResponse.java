package wojia.link.ddns.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DdnsResponse {

    String ip;

}
