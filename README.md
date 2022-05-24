# Ethereum-Sign-Util
[![Maven Central](https://img.shields.io/maven-central/v/com.portto.ethereum/sign.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.portto.ethereum%22%20AND%20a:%22sign%22)
[![CircleCI](https://img.shields.io/circleci/build/github/portto/ethereum-sign-util.kotlin/master)](https://circleci.com/gh/portto/ethereum-sign-util.kotlin/tree/master)
![GitHub](https://img.shields.io/github/license/portto/ethereum-sign-util.kotlin)

A collection of kotlin functions for signing data on Ethereum.

**Ethereum-Sign-Util that is currently under development, alpha builds are available in the [Sonatype staging repository](https://s01.oss.sonatype.org/content/repositories/staging/com/portto/ethereum/sign/).**

## How to
```gradle
repositories {
    mavenCentral()
    
    // If you need to get SolanaWeb3 versions that are not uploaded to Maven Central.
    maven { url "https://s01.oss.sonatype.org/content/repositories/staging/" }
}

dependencies {
    implementation 'com.portto.ethereum:sign:0.1.0'
}
```

## Functions

Create an Ethereum-specific signature for a message.
```kotlin
fun personalSign(privateKey: ByteArray, data: Any): String
```

Sign typed data according to EIP-712. The signing differs based upon the `version`.
```kotlin
fun signTypedData(
    privateKey: ByteArray,
    data: String,
    version: SignTypedDataVersion = SignTypedDataVersion.V4,
): String
```

Hash a typed message according to EIP-712.
```kotlin
fun eip712Hash(typedData: String): ByteArray
```

Generate the "V1" hash for the provided typed message.
```kotlin
fun typedSignatureHash(typedData: TypedDataV1): String
```

### Developed By
Kihon, <kihon@portto.com>

### License
Ethereum-Sign-Util is maintained by [portto](https://github.com/portto/). Licensed under the MIT license.
