package dev.jeonghun.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Board extends BaseEntity {
	@Id
	@Column(name = "board_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(length = 2000, nullable = false)
	private String content;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<Comment> comments = new ArrayList<>();

	@Column(length = 1, nullable = false)
	@ColumnDefault("'N'")
	@Enumerated(EnumType.STRING)
	private final DeleteFlag deleted = DeleteFlag.N;

	@Builder
	private Board(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.changeBoard(this);
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		comment.changeBoard(null);
	}
}
