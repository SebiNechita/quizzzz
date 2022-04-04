package server.api;

import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.bind.annotation.*;
import packets.ResponsePacket;
import packets.ZipRequestPacket;
import server.api.game.ActivityService;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@RequestMapping("/zip")
public class ZipController {
    private final ZipService zipService;
    private final ActivityService activityService;

    /**
     * Constructor for ZIpController
     * @param zipService service layer
     * @param activityService service layer for activityController
     */
    public ZipController(ZipService zipService, ActivityService activityService) {
        this.zipService = zipService;
        this.activityService = activityService;
    }

    //TODO: Replace ResponsePacket constructors

    /**
     * Imports the activities from the zip contained in the given packet
     * @param packet ZipRequestPacket that contains the byte array
     * @return Response with HTTPS Status Created in case of successful creation; HTTP Status InternalServerError in case of failure
     * @throws IOException
     */
    @PostMapping("/")
    public ResponsePacket importZip(@RequestBody ZipRequestPacket packet) throws IOException {
        File activityBank = null;
        try {
            activityBank = new File(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("activity-bank")).toURI()
            );
            FileUtils.cleanDirectory(activityBank);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            LoggerUtil.infoInline("There is no existing activity-bank to clear");
        }
        byte[] bytes = packet.getZipBytes();
//        zipService.constructFile(bytes);
        try {
            zipService.unzip(bytes);
        } catch (IOException e) {
            return new ResponsePacket(HttpStatus.InternalServerError);
        }
        activityService.updateRepository();
        return new ResponsePacket(HttpStatus.Created);
    }



}
