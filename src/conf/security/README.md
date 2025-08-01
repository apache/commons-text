<!--
  ~ Licensed to the Apache Software Foundation (ASF) under one or more
  ~ contributor license agreements.  See the NOTICE file distributed with
  ~ this work for additional information regarding copyright ownership.
  ~ The ASF licenses this file to you under the Apache License, Version 2.0
  ~ (the "License"); you may not use this file except in compliance with
  ~ the License.  You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

# CycloneDX Documents for Apache Commons Text

The Apache Commons Text project publishes multiple [CycloneDX](https://cyclonedx.org/) documents to help consumers assess the security of their applications using this library:

## SBOM (Software Bill of Materials)

Beginning with version `6.6.0`, Apache Commons Text publishes SBOMs in both **XML** and **JSON** formats to Maven Central. These documents describe all components and dependencies of the library, following standard Maven coordinates:

* **Group ID:** `org.apache.commons`
* **Artifact ID:** `commons-text`
* **Classifier:** `cyclonedx`
* **Type:** `xml` or `json`

Each SBOM lists the library’s required and optional dependencies, helping consumers analyze the software supply chain and manage dependency risk.

> [!NOTE]
> The versions listed in the SBOM reflect the dependencies used during the build and test process for that specific release of Text.
> Your own project may use different versions depending on your dependency management configuration.

## VEX (Vulnerability Exploitability eXchange)

An experimental [VEX](https://cyclonedx.org/capabilities/vex/) document is also published:

👉 [`https://raw.githubusercontent.com/apache/commons-text/refs/heads/master/src/conf/security/VEX.cyclonedx.xml`](VEX.cyclonedx.xml)

It is also available in [OpenVEX format](https://github.com/openvex/spec) at:

👉 [`https://raw.githubusercontent.com/apache/commons-text/refs/heads/master/src/conf/security/openvex.json`](openvex.json)

This document provides information about the **exploitability of known vulnerabilities** in the **dependencies** of Apache Commons Text.

### When is a dependency vulnerability exploitable?

Because Apache Commons libraries (including Text) do **not** bundle their dependencies, a vulnerability in a dependency is only exploitable if **both** of the following conditions are true:

1. The vulnerable dependency is included in the consuming project.
2. Apache Commons Text is explicitly listed as affected by the vulnerability.

### Notes and Limitations

* This VEX document is **experimental** and provided **as-is**.
  The semantics of this document may change in the future.
* The **absence** of a vulnerability entry does **not** indicate that Text is unaffected.
* If a version of Text is not listed under the `affects` section of a vulnerability, that version may still be affected or not.
* Only the **latest major version** of Text is currently assessed for vulnerabilities.
* The `analysis` field in the VEX file uses **Markdown** formatting.

For more information about CycloneDX, SBOMs, or VEX, visit [cyclonedx.org](https://cyclonedx.org/).

## Contributing

To add or update a VEX entry:

* Edit the CycloneDX VEX document:
  1. Increase the `version` attribute in the `<bom>` element.
  2. Update the `timestamp` in the `<metadata>` section.
  3. Make your changes to the vulnerability information.
* Regenerate the `openvex.json` file by running the `generate-openvex.sh` script.