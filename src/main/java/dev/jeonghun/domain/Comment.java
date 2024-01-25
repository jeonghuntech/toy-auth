package dev.jeonghun.domain;

import java.util.Objects;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Comment extends BaseEntity {
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 1000, nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	@ToString.Exclude
	private Board board;

	@Column(length = 1, nullable = false)
	@ColumnDefault("'N'")
	@Enumerated(EnumType.STRING)
	private final DeleteFlag deleted = DeleteFlag.N;

	@Builder
	private Comment(String content) {
		this.content = content;
	}

	public void changeBoard(Board board) {
		this.board = board;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Class<?> oEffectiveClass =
			obj instanceof HibernateProxy
				? ((HibernateProxy)obj).getHibernateLazyInitializer().getPersistentClass() : obj.getClass();
		Class<?> thisEffectiveClass =
			this instanceof HibernateProxy ? ((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass() :
				this.getClass();
		if (thisEffectiveClass != oEffectiveClass) {
			return false;
		}
		Comment comment = (Comment)obj;
		return getId() != null && Objects.equals(getId(), comment.getId());
	}

	@Override
	public final int hashCode() {
		if (!(this instanceof HibernateProxy)) {
			return getClass().hashCode();
		}

		return ((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass().hashCode();
	}
}
