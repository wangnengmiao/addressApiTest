package io.terminus.addressApiTest;

import io.terminus.trantor.api.annotation.type.Address;
import io.terminus.trantor.metaStore.address.AddressInfo;
import io.terminus.trantor.metaStore.address.AddressQueryVO;
import io.terminus.trantor.metaStore.address.model.Level;
import io.terminus.trantor.sdk.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class AddressApiTestApplicationTests {

    @Resource
    private AddressFeignClient addressClient;

    @Test
    void contextLoads() {

    }

    /**
     * 刷新地址缓存数据
     */
    @Test
    public void refreshAddressCache() {
        addressClient.refresh();
    }

    /**
     * 根据ID查询地址
     */
    @Test
    public void findAddrssById() {
        Response<AddressInfo> addressResp = addressClient.detail("110000");
        AddressInfo address = addressResp.getRes();
        assertNotNull(address);
    }

    @Test
    public void findChildren() {
        Response<List<AddressInfo>> addressResp = addressClient.queryChildren("110000");
        List<AddressInfo> addressList = addressResp.getRes();
        assertTrue(addressList.size() > 0);
    }

    @Test
    public void findAddressById() {
        Response<Address> addressResp = addressClient.findAddressById("110000");
        Address address = addressResp.getRes();
        List<String> areas = address.getAddress().stream().map(Address.Area::getId).collect(Collectors.toList());
        String[] areaKeys = new String[areas.size()];
        areas.toArray(areaKeys);

        Response<List<AddressInfo>> addressListResp = addressClient.queryEditTree(areaKeys);
        assertTrue(addressListResp.getRes().size() > 0);
    }

    @Test
    public void findAddress() {
        AddressQueryVO addressQueryVO = new AddressQueryVO();
        addressQueryVO.setProvince("北京");
        addressQueryVO.setCity("北京市");
        addressQueryVO.setRegion("东城区");
        assertEquals(addressQueryVO.mergeNames(), "北京,北京市,东城区");
        Response<Address> addressResp = addressClient.findAddress(addressQueryVO.getProvince(),
                addressQueryVO.getCity(), addressQueryVO.getRegion(), addressQueryVO.getStreet(), addressQueryVO.getCommunity());
        Address address = addressResp.getRes();

        assertNotNull(address);
        assertEquals(address.getProvince(), addressQueryVO.getProvince());
        assertEquals(address.getCity(), addressQueryVO.getCity());
        assertEquals(address.getRegion(), addressQueryVO.getRegion());
    }

    @Test
    public void queryFuzzy() {
        Response<List<AddressInfo>> addressResp = addressClient.queryFuzzy("北京", Level.CITY);
        assertTrue(addressResp.getRes().size() > 0);
    }
}
