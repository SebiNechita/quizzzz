package server.api;

import org.springframework.web.bind.annotation.*;
import packets.ResponsePacket;
import packets.ZipRequestPacket;

import java.io.*;

@RestController
@RequestMapping("/zip")
public class ZipController {
    private final ZipService service;

    public ZipController(ZipService service) {
        this.service = service;
    }

    //TODO: Replace ResponsePacket constructors
    @PostMapping("/")
    public ResponsePacket importZip(@RequestBody ZipRequestPacket packet) throws IOException {
        System.out.println("Got the request.");
        byte[] bytes = packet.getZipBytes();
        File file = constructFile(bytes);
        System.out.println(file.toPath());
        try {
            service.unzip();
        } catch (IOException e) {
            return new ResponsePacket(404);
        }
        return new ResponsePacket(200);
    }


    //TODO: move this method to ZipService
    public File constructFile(byte[] bytes) throws IOException {
        File file = new File("server/src/main/resources/uploaded.zip");
        OutputStream
                os
                = new FileOutputStream(file);
        os.write(bytes);
        os.close();
        return file;
    }

}
