package com.example.vendingmachine.dao;


import com.example.vendingmachine.entity.Rack;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class RackDaoTest {

    @Autowired
    RackDao rackDao;

    Rack rack1 = new Rack();
    Rack rack2 = new Rack();

    @Before
    public void before() {

        rackDao.truncate();

        rack1.setRackNo("1");
        rack1.setItemCd("01");
        rack1.setItemNumber(10);
        rack1.setCapacity(15);
        rack1.setPrice(110);
        rackDao.insert(rack1);

        rack2.setRackNo("2");
        rack2.setItemCd("02");
        rack2.setItemNumber(20);
        rack2.setCapacity(25);
        rack2.setPrice(120);
        rackDao.insert(rack2);
    }

    /**
     * Rack全件取得
     */
    @Test
    public void selectAll() {

        List<Rack> actualRackList = rackDao.selectAll();

        assertThat(actualRackList.size(), is(2));

        Rack actualRack1 = actualRackList.get(0);
        Rack actualRack2 = actualRackList.get(1);

        assertThat(actualRack1.getRackNo(), is("1"));
        assertThat(actualRack1.getItemCd(), is("01"));
        assertThat(actualRack1.getItemNumber(), is(10));
        assertThat(actualRack1.getCapacity(), is(15));
        assertThat(actualRack1.getPrice(), is(110));

        assertThat(actualRack2.getRackNo(), is("2"));
        assertThat(actualRack2.getItemCd(), is("02"));
        assertThat(actualRack2.getItemNumber(), is(20));
        assertThat(actualRack2.getCapacity(), is(25));
        assertThat(actualRack2.getPrice(), is(120));
    }

    /**
     * RackNo指定で取得
     */
    @Test
    public void selectOnSale() {

        Rack actualRack = rackDao.selectOne("1");

        assertThat(actualRack.getRackNo(), is("1"));
        assertThat(actualRack.getItemCd(), is("01"));
        assertThat(actualRack.getItemNumber(), is(10));
        assertThat(actualRack.getCapacity(), is(15));
        assertThat(actualRack.getPrice(), is(110));
    }
}
