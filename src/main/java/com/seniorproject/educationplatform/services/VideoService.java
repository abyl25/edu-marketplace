package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.rabbitmq.VideoProcessMessage;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final String basePath = "/home/abylay/Desktop/video-thumbnail-sample";
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    public VideoService() {
        try {
            ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            ffprobe = new FFprobe("/usr/bin/ffprobe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMediaInformation(String videoName) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(basePath + "/" + videoName);
        FFmpegFormat format = probeResult.getFormat();
        System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs",
                format.filename,
                format.format_long_name,
                format.duration
        );
        FFmpegStream stream = probeResult.getStreams().get(0);
        System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx",
                stream.codec_long_name,
                stream.width,
                stream.height
        );
    }

    public void processVideo(VideoProcessMessage videoMessage) throws IOException {
        logger.info("processVideo(), Thread name: " + Thread.currentThread().getName());
        this.createVideoThumbnail(videoMessage.getPath(), videoMessage.getVideoName());

        FFmpegProbeResult probeResult = ffprobe.probe(videoMessage.getPath() + "/" + videoMessage.getVideoName() + "." + videoMessage.getExtension());
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(probeResult)     // Filename, or a FFmpegProbeResult
            .overrideOutputFiles(true) // Override the output if it exists
            .addOutput(videoMessage.getPath() + "/" + videoMessage.getVideoName() + "_" + videoMessage.getWidth() + "x" + videoMessage.getHeight() + "." + videoMessage.getExtension())   // Filename for the destination
            .setFormat("mp4")        // Format is inferred from filename, or can be set
            .setTargetSize(250_000)  // Aim for a 250KB file
            .disableSubtitle()       // No subtiles
            .setAudioChannels(2)         // Mono audio
            .setAudioCodec("aac")        // using the aac codec
            .setAudioSampleRate(48_000)  // at 48KHz
            .setAudioBitRate(384000)      // at 192 kbit/s
            .setVideoCodec("libx264")     // Video using x264
            .setVideoFrameRate(30, 1)     // at 24 frames per second
            .setVideoBitRate(8000000)
            .setVideoResolution(1920, 1080)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public void createVideoThumbnail(String path, String videoName) throws IOException {
//        FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");

        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(path + "/" + videoName + ".mp4")
            .addOutput(path + "/" + videoName + ".png")
            .setFrames(1)
            .setStartOffset(1000, TimeUnit.MILLISECONDS)
            .setVideoFilter("select='gte(n\\,10)',scale=1000:-1")
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();
    }

    private FFmpegBuilder videoThumbnailBuilder(String input, String output, long offset) {
        return new FFmpegBuilder()
                .setInput(input)
                .addOutput(output)
                .setFrames(1)
                .setStartOffset(0, TimeUnit.MILLISECONDS)
                .setVideoFilter("select='gte(n\\,10)',scale=700:-1")
                .done();
    }
}
