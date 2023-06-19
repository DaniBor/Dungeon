package core.components;

import core.Component;
import core.Entity;

/**
 * The CameraComponent marks an entity as the point of focus for the camera.
 *
 * <p>If an entity has this Component, the {@link core.systems.CameraSystem} will center on the
 * position of the entity, if the entity also has a {@link PositionComponent}.
 *
 * <p>Note: Currently, it is not recommended to have multiple Entities with a CameraComponent at the
 * same time because the {@link core.systems.CameraSystem} can only focus on one entity. The process
 * of deciding which entity to focus on is not deterministic.
 *
 * <p>If you want to switch the focus to another Entity, simply remove the Component from the first
 * Entity and add a new CameraComponent to the new one.
 *
 * @see core.systems.CameraSystem
 */
public final class CameraComponent extends Component {

    /**
     * Create a new CameraComponent and add it to the associated entity.
     *
     * @param entity entity which should be the focus point of the camera
     */
    public CameraComponent(final Entity entity) {
        super(entity);
    }
}
