package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.file.VideoDto;
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
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
//    private final String basePath = "/home/abylay/Desktop/video-thumbnail-sample";
    private final String basePath = "/var/www/edu-marketplace";
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

    public VideoDto getMediaInformation(String videoName, Path path) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(path.resolve(videoName).toString());
        FFmpegFormat format = probeResult.getFormat();
        FFmpegStream stream = probeResult.getStreams().get(0);

        VideoDto videoDto = new VideoDto();
        videoDto.setFullName(format.filename);
        videoDto.setFormat(format.format_name);
        videoDto.setLongFormat(format.format_long_name);
        videoDto.setDuration(Math.floor(format.duration));
        videoDto.setSize(format.size);
        videoDto.setHeight(stream.height);
        videoDto.setWidth(stream.width);
        videoDto.setDisplayAspectRatio(stream.display_aspect_ratio);
        videoDto.setFrames(stream.avg_frame_rate.getNumerator());
        videoDto.setVideoBitrate(stream.bit_rate);
        videoDto.setVideoCodec(stream.codec_name);
        videoDto.setVideoCodecLong(stream.codec_long_name);
        System.out.println(videoDto);
        return videoDto;
    }

    public void processVideo(VideoProcessMessage videoMessage) throws IOException {
        logger.info("processVideo(), Thread name: " + Thread.currentThread().getName());
//        this.createVideoThumbnail(videoMessage.getPath(), videoMessage.getVideoName(), "png");

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

    public void createVideoThumbnail(Path path, String videoName, String videoFormat, String imageFormat) throws IOException {
        String input = path.resolve(videoName + "." + videoFormat).toString();
        String output = path.resolve(videoName + "." + imageFormat).toString();
        int frames = 1;
        long offset = 1000;
        int scale = 1000;
        FFmpegBuilder builder = this.videoThumbnailBuilder(input, output, frames, offset, scale);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();
    }

    private FFmpegBuilder videoThumbnailBuilder(String input, String output, int frames, long offset, int scale) {
        return new FFmpegBuilder()
                .setInput(input)
                .addOutput(output)
                .setFrames(frames)
                .setStartOffset(offset, TimeUnit.MILLISECONDS)
                .setVideoFilter("select='gte(n\\,10)',scale=" + scale + ":-1")
                .done();
    }
}
