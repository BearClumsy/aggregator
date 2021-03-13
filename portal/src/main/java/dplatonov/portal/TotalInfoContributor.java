package dplatonov.portal;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TotalInfoContributor implements InfoContributor {
    private final BuildProperties buildProperties;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> details = new HashMap<>();
        details.put("author", buildProperties.getGroup());
        builder.withDetail("info", details);
    }
}
