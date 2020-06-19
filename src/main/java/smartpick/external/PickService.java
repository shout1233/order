
package smartpick.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@FeignClient(name="smartpick", url="${api.url.smartpick}")
public interface PickService {

    @RequestMapping(method= RequestMethod.GET, path="/picks/getPickYn")
    public @ResponseBody Pick inquiry(@RequestParam Long orderId);

}