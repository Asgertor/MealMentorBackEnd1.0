package dat3.openai_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import security.entity.Role;
import security.entity.UserWithRoles;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor @Getter @Setter @AllArgsConstructor

@Entity
@Table(name="Users")
public class User extends UserWithRoles {

    int weight;
    int height;
    int age;
    @ManyToMany
    @JoinTable(
            name = "user_allergy", // Navnet på join-tabellen
            joinColumns = @JoinColumn(name = "username_id"), // Kolonnen i join-tabellen, der refererer til `User`
            inverseJoinColumns = @JoinColumn(name = "allergy_name") // Kolonnen i join-tabellen, der refererer til `Allergy`
    )
    List<Allergy> allergies;
    String sex;
    @Column(name="activity_level")
    String activityLevel;
    String goals;


    public User(String username, String password, String email,
                int weight, int height, int age, List<Allergy> allergies, String sex,
                String activityLevel, String goals) {

        super(username, password, email);
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.allergies = allergies;
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.goals = goals;
    }
}

