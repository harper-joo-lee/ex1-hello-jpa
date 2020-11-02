package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // db당 하나만 생성해서 애플리케이션 전체에서 공유
        // persistence.xml 에서 설정한 name을 가져온다 ( persistenceUnitName)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // 고객의 요청이 올때마다 계속 사용할 수있다. 쓰레드간 공유 X (사용하고 버려야 한다.)
        EntityManager em = emf.createEntityManager();
        // 모든 데이터의 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
// 등록
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");

//            em.persist(member);

// 조회
            // 첫번째 파라미터 : 엔티티 클래스
//            Member findMember = em.find(Member.class, 1L );
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

/*JPQL :JPA가 제공하는 SQL을 추상화한 객체지향 쿼리언어
* 간단한 조회가 아닌 조인이 필요한 데이터를 찾아야한다면 .. JPQL를 사용할 수있다.
* JPA를 사용하면 엔티티 객체를 중심으로 개발.
* */
            // JPA 를 짤떄 테이블을 대상으로 하는것이 아닌 객체가 대상이 된다. ex) Member 객체
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                                .setFirstResult(1)  // 페이지네이션
                                .setMaxResults(10)
                                .getResultList();

           for (Member member : result) {
               System.out.print("member.name = " + member.getName());
           }

            // 수정
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJpa");
            // -> em.persist 처럼 저장 안해도 실행하면 update 쿼리가 나간다.
            // A : JPA 를 통해 엔티티를 가져오면 JPA를 관리한다. 변경이 안됐는지 체크하고 변경이되면 업데이트 쿼리를 날린다.



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            // 사용후 close.
            em.close();
        }

        emf.close();
    }

}
