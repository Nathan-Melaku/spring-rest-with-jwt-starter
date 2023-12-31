package et.nate.poll.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private String country;
        private String state;
        private String street;
        private String postalCod;
}
