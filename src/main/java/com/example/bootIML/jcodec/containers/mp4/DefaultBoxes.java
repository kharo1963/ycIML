package com.example.bootIML.jcodec.containers.mp4;

import com.example.bootIML.jcodec.containers.mp4.boxes.Box;
import com.example.bootIML.jcodec.containers.mp4.boxes.Box.LeafBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import com.example.bootIML.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.ClearApertureBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.ClipRegionBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.CompositionOffsetsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.DataInfoBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.DataRefBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.EditListBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.EditsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.EncodedPixelBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.FileTypeBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.GenericMediaInfoBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.HandlerBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.IListBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.KeysBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.LoadSettingsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MediaBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MediaHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MediaInfoBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MetaBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieExtendsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieExtendsHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieFragmentBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieFragmentHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.MovieHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.NameBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.NodeBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.PartialSyncSamplesBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.ProductionApertureBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SegmentIndexBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SegmentTypeBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SoundMediaHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.SyncSamplesBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TimeToSampleBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TimecodeMediaInfoBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrackExtendsBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrackFragmentBaseMediaDecodeTimeBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrackFragmentBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrackFragmentHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrackHeaderBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrakBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.TrunBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.UdtaBox;
import com.example.bootIML.jcodec.containers.mp4.boxes.VideoMediaHeaderBox;

public class DefaultBoxes extends Boxes {
    public DefaultBoxes() {
        super();
        mappings.put(MovieExtendsBox.fourcc(), MovieExtendsBox.class);
        mappings.put(MovieExtendsHeaderBox.fourcc(), MovieExtendsHeaderBox.class);
        mappings.put(SegmentIndexBox.fourcc(), SegmentIndexBox.class);
        mappings.put(SegmentTypeBox.fourcc(), SegmentTypeBox.class);
        mappings.put(TrackExtendsBox.fourcc(), TrackExtendsBox.class);
        mappings.put(VideoMediaHeaderBox.fourcc(), VideoMediaHeaderBox.class);
        mappings.put(FileTypeBox.fourcc(), FileTypeBox.class);
        mappings.put(MovieBox.fourcc(), MovieBox.class);
        mappings.put(MovieHeaderBox.fourcc(), MovieHeaderBox.class);
        mappings.put(TrakBox.fourcc(), TrakBox.class);
        mappings.put(TrackHeaderBox.fourcc(), TrackHeaderBox.class);
        mappings.put("edts", EditsBox.class);
        mappings.put(EditListBox.fourcc(), EditListBox.class);
        mappings.put(MediaBox.fourcc(), MediaBox.class);
        mappings.put(MediaHeaderBox.fourcc(), MediaHeaderBox.class);
        mappings.put(MediaInfoBox.fourcc(), MediaInfoBox.class);
        mappings.put(HandlerBox.fourcc(), HandlerBox.class);
        mappings.put(DataInfoBox.fourcc(), DataInfoBox.class);
        mappings.put("stbl", NodeBox.class);
        mappings.put(SampleDescriptionBox.fourcc(), SampleDescriptionBox.class);
        mappings.put(TimeToSampleBox.fourcc(), TimeToSampleBox.class);
        mappings.put(SyncSamplesBox.STSS, SyncSamplesBox.class);
        mappings.put(PartialSyncSamplesBox.STPS, PartialSyncSamplesBox.class);
        mappings.put(SampleToChunkBox.fourcc(), SampleToChunkBox.class);
        mappings.put(SampleSizesBox.fourcc(), SampleSizesBox.class);
        mappings.put(ChunkOffsetsBox.fourcc(), ChunkOffsetsBox.class);
        mappings.put("keys", KeysBox.class);
        mappings.put(IListBox.fourcc(), IListBox.class);
        mappings.put("mvex", NodeBox.class);
        mappings.put("moof", NodeBox.class);
        mappings.put("traf", NodeBox.class);
        mappings.put("mfra", NodeBox.class);
        mappings.put("skip", NodeBox.class);
        mappings.put(MetaBox.fourcc(), MetaBox.class);
        mappings.put(DataRefBox.fourcc(), DataRefBox.class);
        mappings.put("ipro", NodeBox.class);
        mappings.put("sinf", NodeBox.class);
        mappings.put(ChunkOffsets64Box.fourcc(), ChunkOffsets64Box.class);
        mappings.put(SoundMediaHeaderBox.fourcc(), SoundMediaHeaderBox.class);
        mappings.put("clip", NodeBox.class);
        mappings.put(ClipRegionBox.fourcc(), ClipRegionBox.class);
        mappings.put(LoadSettingsBox.fourcc(), LoadSettingsBox.class);
        mappings.put("tapt", NodeBox.class);
        mappings.put("gmhd", NodeBox.class);
        mappings.put("tmcd", Box.LeafBox.class);
        mappings.put("tref", NodeBox.class);
        mappings.put(ClearApertureBox.CLEF, ClearApertureBox.class);
        mappings.put(ProductionApertureBox.PROF, ProductionApertureBox.class);
        mappings.put(EncodedPixelBox.ENOF, EncodedPixelBox.class);
        mappings.put(GenericMediaInfoBox.fourcc(), GenericMediaInfoBox.class);
        mappings.put(TimecodeMediaInfoBox.fourcc(), TimecodeMediaInfoBox.class);
        mappings.put(UdtaBox.fourcc(), UdtaBox.class);
        mappings.put(CompositionOffsetsBox.fourcc(), CompositionOffsetsBox.class);
        mappings.put(NameBox.fourcc(), NameBox.class);
        mappings.put("mdta", LeafBox.class);

        mappings.put(MovieFragmentHeaderBox.fourcc(), MovieFragmentHeaderBox.class);
        mappings.put(TrackFragmentHeaderBox.fourcc(), TrackFragmentHeaderBox.class);
        mappings.put(MovieFragmentBox.fourcc(), MovieFragmentBox.class);
        mappings.put(TrackFragmentBox.fourcc(), TrackFragmentBox.class);
        mappings.put(TrackFragmentBaseMediaDecodeTimeBox.fourcc(), TrackFragmentBaseMediaDecodeTimeBox.class);
        mappings.put(TrunBox.fourcc(), TrunBox.class);
    }

}
