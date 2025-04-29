package jeet.gaekwad.samplegnec_1.Service.TTSIntegrater;

import jeet.gaekwad.samplegnec_1.DTOs.TTSRequestDTO;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class TTSServiceImpl implements TTSService {

    private final String mimic3URL = "http://mimic:59125/api/tts";

    @Override
    public byte[] generateAndSaveAudioFile(TTSRequestDTO text) throws IOException {
        String cleanedText = text.getText().replace("text","").trim();
        String JsonInput = "{ \"" + cleanedText + "\" }";

        //Setup HTTP Connection
        URL url = new URL(mimic3URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(JsonInput.getBytes(StandardCharsets.UTF_8));
        }
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed TO generate Audio File : HTTP error code : " + conn.getResponseCode());
        }
        byte[] wavbytes;
        try (InputStream is = conn.getInputStream()) {
            wavbytes = is.readAllBytes();
        }
        return convertWavtoMp3(wavbytes);
    }

    private byte[] convertWavtoMp3(byte[] wavBytes) throws IOException {
        File tempFileWav = File.createTempFile("temp", ".wav");
        File tempFileMp3 = File.createTempFile("temp", ".mp3");

        Files.write(tempFileWav.toPath(), wavBytes);

        //Run ffmpeg command
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-y", "-i", tempFileWav.getAbsolutePath(), tempFileMp3.getAbsolutePath());
        pb.redirectErrorStream(true);
        Process p = pb.start();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while(reader.readLine() != null) {

            }
        }
        int exitCode;
        try {
            exitCode = p.waitFor();
        }catch(InterruptedException e) {
            throw new IOException("ffmpeg interrupted" , e);
        }

        if (exitCode != 0) {
            throw new IOException("ffmpeg failed to convert the file " + exitCode);
        }

        byte[] mp3Bytes = Files.readAllBytes(tempFileMp3.toPath());

        tempFileWav.delete();
        tempFileMp3.delete();
        return mp3Bytes;
    }
}
