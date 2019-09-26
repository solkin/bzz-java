## Bzz archiver Java [![Build Status](https://travis-ci.org/solkin/bzz-java.svg?branch=master)](https://travis-ci.org/solkin/bzz-java)
Based on Huffman tree file compressor/decompressor

### Command line options
`-c <source> <archive>` - compress file `source` to file `archive`.

`-x <archive> <extracted>` - extract archive from file `archive` to file `extracted`.

### Archive file format
`16-bit big endian` - archive version (for now is `0x01`)

`32-bit big endian` - dictionary size

Array of dictionary items:

`8-bit` - dictionary value

`32-bit big endian` - value frequency

Archive body in bit-by-bit format
