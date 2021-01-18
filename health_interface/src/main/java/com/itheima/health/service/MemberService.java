package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/12
 */
public interface MemberService {
    /**
     * 通过手机号码判断是否为会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 通过手机号码判断是否为会员
     * @param member
     */
    void add(Member member);

    /**
     * 统计每个月的会员总数量
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
