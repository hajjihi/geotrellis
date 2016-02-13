package geotrellis.raster.io.geotiff

import geotrellis.raster._
import geotrellis.raster.io.geotiff.compression._

import java.nio.ByteBuffer

class Float64GeoTiffMultiBandTile(
  compressedBytes: Array[Array[Byte]],
  decompressor: Decompressor,
  segmentLayout: GeoTiffSegmentLayout,
  compression: Compression,
  bandCount: Int,
  hasPixelInterleave: Boolean,
  noDataValue: Option[Double]
) extends GeoTiffMultiBandTile(compressedBytes, decompressor, segmentLayout, compression, bandCount, hasPixelInterleave, noDataValue)
    with Float64GeoTiffSegmentCollection {

  protected def createSegmentCombiner(targetSize: Int): SegmentCombiner =
    new SegmentCombiner(bandCount) {
      private val arr = Array.ofDim[Double](targetSize)

      def set(targetIndex: Int, v: Int): Unit = {
        arr(targetIndex) = i2d(v)
      }

      def setDouble(targetIndex: Int, v: Double): Unit = {
        arr(targetIndex) = v
      }

      def getBytes(): Array[Byte] = {
        val result = new Array[Byte](targetSize * TypeDouble.bytes)
        val bytebuff = ByteBuffer.wrap(result)
        bytebuff.asDoubleBuffer.put(arr)
        result
      }
    }
}

