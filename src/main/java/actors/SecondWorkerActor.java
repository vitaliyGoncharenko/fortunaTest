package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SecondWorkerActor extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecondWorkerActor.class);

    @Override
    public void preStart() throws Exception {
        LOGGER.info("Start SecondWorkerActor");
    }

    @Override
    public void onReceive(Object hashMap) throws Exception {
        StringBuilder sb = null;
        if (hashMap instanceof HashMap) {
            sb = new StringBuilder();
            for (int i = 1; i < ((HashMap) hashMap).size() + 1; i++) {
                sb.append("\"").append(i).append(";").append(((HashMap) hashMap).get(i)).append("\";\n");
            }
        }
        sender().tell(sb.toString(), ActorRef.noSender());
    }

    @Override
    public void postStop() throws Exception {
        LOGGER.info("SecondWorkerActor stop");
    }
}
