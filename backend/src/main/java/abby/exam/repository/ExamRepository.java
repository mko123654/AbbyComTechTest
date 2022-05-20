package abby.exam.repository;

import abby.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, String> {
    @Query("select e from Exam e order by e.updateTime desc")
    List<Exam> findAll();
}
