/*
 * Copyright (C) 2022 portto Co., Ltd.
 *
 * Created by Kihon on 2022/5/24
 */

package com.portto.ethereum.sign

import org.junit.Assert
import org.junit.Test
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.khex.model.HexString

class EthSigUtilTest {

    @Test
    fun sign() {
        val privateKey = HexString("28b196627f14613d3940a9d532d5c51190f18cf5ab49f281a308de2d3414b43d").hexToByteArray()

        val message = """[{"type":"string[]","name":"message","value":["Hi, Alice!","Wooooo"]},{"type":"uint8[]","name":"value","value":[1,2]},{"type":"bytes","name":"message","value":"0xdeadbeaf"},{"type":"address","name":"wallet","value":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"}]"""
        val typedSignatureHash = EthSigUtil.typedSignatureHash(message)
        Assert.assertEquals(typedSignatureHash,
            "0x6f1843999916944e2e4565adf572027accbfd43ec9e8f59a82baf0e7a7897873")

        val eip712Hash = EthSigUtil.signTypedData(privateKey, message, SignTypedDataVersion.V1)
        Assert.assertEquals(eip712Hash,
            "0x8bfba589103bfeac3ff27dfaffc1c1c64f4d696ba63fb0e7c36f40c168dcd037439ec9da6a9a4d76919bda3b20df0ddb4a41345ad03d5a5179edbb13e31b9d3f1b")

        val messageV3 = """{"types":{"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}],"Person":[{"name":"name","type":"string"},{"name":"wallet","type":"address"}],"Mail":[{"name":"from","type":"Person"},{"name":"to","type":"Person"},{"name":"contents","type":"string"}]},"primaryType":"Mail","domain":{"name":"Ether Mail","version":"1","chainId":4,"verifyingContract":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"},"message":{"from":{"name":"Cow","wallet":"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826"},"to":{"name":"Bob","wallet":"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB"},"contents":"Hello, Bob!"}}"""
        val eip712HashV3 = EthSigUtil.signTypedData(privateKey, messageV3)
        Assert.assertEquals(eip712HashV3,
            "0x49dc70f40f72088bb44ed22678fa0cac21305113b7ed6d9dcb3adfe656d21c6311b4e7ff65000d4b8ba89ea983e9fd4d857e2d3e888d632d657f56b4d4ff4eb01b")

        val messageV4 = """{"domain":{"chainId":4,"name":"Ether Mail","verifyingContract":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC","version":"1"},"message":{"contents":"Hello, Bob!","from":{"name":"Cow","wallets":["0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826","0xDeaDbeefdEAdbeefdEadbEEFdeadbeEFdEaDbeeF"]},"to":[{"name":"Bob","wallets":["0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB","0xB0BdaBea57B0BDABeA57b0bdABEA57b0BDabEa57","0xB0B0b0b0b0b0B000000000000000000000000000"]}]},"primaryType":"Mail","types":{"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}],"Group":[{"name":"name","type":"string"},{"name":"members","type":"Person[]"}],"Mail":[{"name":"from","type":"Person"},{"name":"to","type":"Person[]"},{"name":"contents","type":"string"}],"Person":[{"name":"name","type":"string"},{"name":"wallets","type":"address[]"}]}}"""
        val eip712HashV4 = EthSigUtil.eip712Hash(messageV4).toNoPrefixHexString()
        Assert.assertEquals(eip712HashV4,
            "edeff92a8dcf61f70eb77c8cc8291a38779616bcf47f48adce044b8c9347aa7c")
        val signTypedDataHash = EthSigUtil.signTypedData(privateKey, messageV4)
        Assert.assertEquals(signTypedDataHash,
            "0x40006c4a9f6d7d79ba9e4117368dd392789aa8b449479e689b8e544a3884e20052a2da368c084560b216ad02b27530ed8c6900eb763fb971d25b713e6d0e7ebf1b")

        val personalMessage = "Any Message you wanna sign"
        Assert.assertEquals(EthSigUtil.personalSign(privateKey, personalMessage),
            "0xfb1e2bf6bc06459d63d3a37af547f6ea70a3d131a90f6ea0e86866d7e9017c3f0622ac1c08c2bcbcdb4880a51a60616ab7c4ba7c0a0927cc8b8a4f98614e34711b")

        val personalMessage0xPrefixed = "0x416e79204d65737361676520796f752077616e6e61207369676e"
        Assert.assertEquals(EthSigUtil.personalSign(privateKey, personalMessage0xPrefixed),
            "0xfb1e2bf6bc06459d63d3a37af547f6ea70a3d131a90f6ea0e86866d7e9017c3f0622ac1c08c2bcbcdb4880a51a60616ab7c4ba7c0a0927cc8b8a4f98614e34711b")

        val personalMessageNon0xPrefixed = "416e79204d65737361676520796f752077616e6e61207369676e"
        Assert.assertEquals(EthSigUtil.personalSign(privateKey, personalMessageNon0xPrefixed),
            "0x2c2115cacbc00cbcf3db3995668f83aef86e69e5b501545fdf700cfcebb405a16d9432fd24d9761ead3963e9d0e1ae2ae40cf7c976d736bb544c7f8692d766d51c")

    }

}