package io.terminus.addressApiTest;

import io.terminus.trantor.api.annotation.type.Address;
import io.terminus.trantor.metaStore.address.AddressInfo;
import io.terminus.trantor.metaStore.address.AddressQueryVO;
import io.terminus.trantor.metaStore.address.model.Level;
import io.terminus.trantor.sdk.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author DongZhao
 * Date:2020/11/26
 */
@FeignClient(name = "myFeignClient",path ="/api/trantor/address", url = "http://127.0.0.1:8082")
public interface AddressFeignClient {
    /**
     * 刷新地址缓存数据
     * @return
     */
    @GetMapping("/refresh")
    Response<?> refresh();

    /**
     * 根据ID查询地址详情
     * @param id
     * @return
     */
    @GetMapping("/query/detail")
    Response<AddressInfo> detail(@RequestParam String id) ;

    /**
     * 根据ID查询下级地址列表
     * @param parentId
     * @return
     */
    @GetMapping("/query/children")
    Response<List<AddressInfo>> queryChildren(@RequestParam String parentId) ;

    /**
     * 根据ID获取标准地址信息
     * @param id
     * @return
     */
    @GetMapping("/findAddressById")
    Response<Address> findAddressById(@RequestParam String id);

    /**
     * 根据省、市、区、街道获取标准地址信息
     * @return
     */
    @GetMapping("/findAddress")
    Response<Address> findAddress(@RequestParam(required = false) String province,
                                  @RequestParam(required = false) String city,
                                  @RequestParam(required = false) String region,
                                  @RequestParam(required = false) String street,
                                  @RequestParam(required = false) String community) ;

    /**
     * 地址编辑时，返回地址树结构（不是完整结构，而仅包括被命中的各层级的兄弟节点）
     *
     * @param keys
     * @return
     */
    @RequestMapping("/query/editTree")
    Response<List<AddressInfo>> queryEditTree(String[] keys);

    /**
     * 地址模糊查询
     * @param key
     * @param level
     * @return
     */
    @GetMapping("/query/fuzzy")
    Response<List<AddressInfo>> queryFuzzy(@RequestParam String key, @RequestParam(required = false) Level level);
}
