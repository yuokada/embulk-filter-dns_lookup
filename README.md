# Dns Lookup filter plugin for Embulk

## Overview

* **Plugin type**: filter

## Configuration

- **columns**: description (list<string>, default: `null`)

## Example

```yaml
filters:
  - type: dns_lookup
    columns:
      - ipaddress
```

```bash
% cat example/example.yml
in:
 type: file
 path_prefix: example/demo.csv
 parser:
   type: csv
   charset: UTF-8
   newline: CRLF
   null_string: 'NULL'
   skip_header_lines: 0
   comment_line_marker: '#'
   columns:
    - {name: ipaddress,     type: string}

filters:
  - type: dns_lookup
    columns:
      - ipaddress

out:
  type: stdout

% cat example/demo.csv
183.79.248.124
93.184.216.34
192.30.255.112
192.30.255.113
182.22.59.229
183.79.135.206

% embulk run -I lib example/example.yml
2017-09-21 22:24:30.532 +0900: Embulk v0.8.34
2017-09-21 22:24:35.602 +0900 [INFO] (0001:transaction): Loaded plugin embulk/filter/dns_lookup from a load path
2017-09-21 22:24:35.635 +0900 [INFO] (0001:transaction): Listing local files at directory 'example' filtering filename by prefix 'demo.csv'
2017-09-21 22:24:35.639 +0900 [INFO] (0001:transaction): "follow_symlinks" is set false. Note that symbolic links to directories are skipped.
2017-09-21 22:24:35.646 +0900 [INFO] (0001:transaction): Loading files [example/demo.csv]
2017-09-21 22:24:35.714 +0900 [INFO] (0001:transaction): Using local thread executor with max_threads=8 / output tasks 4 = input tasks 1 * 4
2017-09-21 22:24:35.723 +0900 [INFO] (0001:transaction): {done:  0 / 1, running: 0}
edge2000.img.vip.djm.yimg.jp
93.184.216.34
lb-192-30-255-112-sea.github.com
lb-192-30-255-113-sea.github.com
182.22.59.229
183.79.135.206
2017-09-21 22:24:35.851 +0900 [INFO] (0001:transaction): {done:  1 / 1, running: 0}
2017-09-21 22:24:35.859 +0900 [INFO] (main): Committed.
2017-09-21 22:24:35.860 +0900 [INFO] (main): Next config diff: {"in":{"last_path":"example/demo.csv"},"out":{}}
```


## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
