import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {PrimaryButton} from "Frontend/components/PrimaryButton";
import {SandboxEndpoint} from "Frontend/generated/endpoints";
import {useState} from "react";
import {Subscription} from "@hilla/frontend";

export function TestView() {

    const [connection, setConnection] = useState<Subscription<string> | undefined>(undefined);
    const [lastMessage, setLastMessage] = useState("");
    return (
        <VerticalLayout>
            <PrimaryButton onClick={() => {
                setConnection(
                    SandboxEndpoint.openConnection()
                        .onNext(setLastMessage)
                        .onError(() => console.log("error"))
                        .onComplete(() => console.log("complete"))
                );
            }}>Subscribe</PrimaryButton>
            <PrimaryButton disabled={connection === undefined} onClick={() => {
                connection?.cancel()
                setConnection(undefined)
            }}>Cancel</PrimaryButton>

            <h6>{connection === undefined ? "undefined" : "present"}</h6>
            <h6>{lastMessage}</h6>
        </VerticalLayout>
    );
}
