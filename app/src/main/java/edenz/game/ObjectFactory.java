package edenz.game;

/**
 * Created by Nishshanka on 6/26/17.
 */
import edenz.game.*;

public class ObjectFactory {

    private static ObjectFactory instance = null;

    private ObjectFactory(){}

    public ObjectFactory getObjectFactoryInstance(){
        if(instance == null)
            instance = new ObjectFactory();

        return instance;
    }

    public GameObject getGameObject(GameObjectType type)
    {
         GameObject object = null;

         //if(type == GameObjectType.BALL)
            //object = new Player();
         //if(type == GameObjectType.PIPE)
            //object == new Pipe();
         //if(type == GameObjectType.FIRE)
                //object = new Fire();
         //if(type == GameObjectType.MISSILE)
            //object == new Missile();
         // if(type == GameObjectType.EXPLOSION)
             //object = new explosion();

        return object;

    }
}
