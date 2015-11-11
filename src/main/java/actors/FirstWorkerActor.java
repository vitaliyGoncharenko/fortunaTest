package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstWorkerActor extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstWorkerActor.class);
    private ActorRef secondWorkerActor;

    Map<Integer, Integer> hashMapList = new HashMap<Integer, Integer>(1000);

    @Override
    public void preStart() throws Exception {
        LOGGER.info("Start FirstWorkerActor");
        secondWorkerActor = context().actorOf(Props.create(SecondWorkerActor.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String regexp = "([0-9]+);([0-9]+)";

            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(message.toString());
            if (matcher.find()) {
                int index = Integer.valueOf(matcher.group(1));
                int amount = Integer.valueOf(matcher.group(2));

                if (hashMapList.containsKey(index)) {
                    int currentAmount = hashMapList.get(index);
                    hashMapList.put(index, currentAmount + amount);
                } else {
                    hashMapList.put(index, amount);
                }
            }
            if (message.equals("Reading from a file is complete")) {
                secondWorkerActor.tell(hashMapList, sender());
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        LOGGER.info("FirstWorkerActor stop");
    }
}
