package server.api;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.bind.annotation.*;
import packets.ResponsePacket;
import packets.ZipRequestPacket;
import server.api.game.ActivityService;

import java.io.*;

@RestController
@RequestMapping("/zip")
public class ZipController {
    private final ZipService zipService;
    private final ActivityService activityService;

    public ZipController(ZipService zipService, ActivityService activityService) {
        this.zipService = zipService;
        this.activityService = activityService;
    }

    //TODO: Replace ResponsePacket constructors
    @PostMapping("/")
    //Imports the activities from the zip contained in the given packet
    public ResponsePacket importZip(@RequestBody ZipRequestPacket packet) throws IOException {
        byte[] bytes = packet.getZipBytes();
        zipService.constructFile(bytes);
        FileUtils.cleanDirectory(new File("server/src/main/resources/activity-bank"));
        try {
            zipService.unzip();
        } catch (IOException e) {
            return new ResponsePacket(404);
        }
        activityService.updateRepository();
        return new ResponsePacket(200);
    }



}
