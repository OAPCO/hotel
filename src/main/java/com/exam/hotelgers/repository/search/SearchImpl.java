package com.exam.hotelgers.repository.search;

import com.exam.hotelgers.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SearchImpl extends QuerydslRepositorySupport implements Search {
    public SearchImpl() {
        super(Store.class);
    }

    @Override
    public Page<Store> selectSearch(String distName, String branchName,String brandName, Pageable pageable) {

        QStore store = QStore.store;
        QDist dist = QDist.dist;
        QBranch branch = QBranch.branch;


        JPQLQuery<Store> storeJPQLQuery = from(store);
        JPQLQuery<Dist> distJPQLQuery = from(dist);
        JPQLQuery<Branch> branchJPQLQuery = from(branch);


        BooleanBuilder booleanBuilder = new BooleanBuilder();


        //가져온 "distName"값이 null이 아니면
        if(distName != null){

            //불린빌더에 다음과 같은 and 조건을 추가한다 : store테이블의 dist의 distName이 가져온 "distName" 과 일치하는가?
            booleanBuilder.and(store.dist.distName.contains(distName));
        }

        if(branchName != null){
            booleanBuilder.and(store.branch.branchName.contains(branchName));
        }

        if(brandName != null){
            booleanBuilder.and(store.brand.brandName.contains(brandName));
        }


        storeJPQLQuery.where(booleanBuilder); //조건 비교를 모두 끝낸 뒤 빌더 넣기

        this.getQuerydsl().applyPagination(pageable,storeJPQLQuery);

        List<Store> list = storeJPQLQuery.fetch();
        long count = storeJPQLQuery.fetchCount();

        return new PageImpl<>(list,pageable,count);
    }
}
