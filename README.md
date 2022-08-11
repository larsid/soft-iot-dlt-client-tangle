# soft-iot-dlt-client-tangle

O `soft-iot-dlt-client-tangle` é o _bundle_ responsável por provê a serviços de escrita e leitura na rede Tangle. Ele também especifica um conjunto de transações que são utilizados no protocolo de balanceamento de carga.

# Instalação

Para instalar o `client-tangle` é necessário configurar o repositório fonte e em seguida executar o seguinte comando no terminal do servicemix.

    bundle:install mvn:com.github.larsid/soft-iot-dlt-client-tangle/main

# Configurações

| Propriedade    | Descrição                                                                                                   | Valor padrão                                                                                                                         |
| -------------- | ----------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| DLT_PROTOCOL   | Define qual é o tipo de protocolo utilizado pelo cliente da API.                                            | https                                                                                                                                |
| DLT_URL        | Define qual é a URL do nó da rede que o cliente deve se conectar.                                           | nodes.devnet.iot.org                                                                                                                 |
| DLT_PORT       | Define a porta.                                                                                             | 443                                                                                                                                  |
| ADDRESS        | Define o endereço da transação.                                                                             | [Verificar aqui](https://github.com/larsid/soft-iot-dlt-client-tangle/blob/master/src/main/resources/soft.iot.dlt.client.tangle.cfg) |
| DEPTH          | -                                                                                                           | 3                                                                                                                                    |
| MWM            | -                                                                                                           | 9                                                                                                                                    |
| SECURITY_LEVEL | -                                                                                                           | 2                                                                                                                                    |
| VALUE          |                                                                                                             | 0                                                                                                                                    |
| BUFFER_MAX_LEN | Define o tamanho máximo do `buffer` que armazena as transações que serão enviadas para a rede.              | 128                                                                                                                                  |
| ZMQ_SOCKET_PROTOCOL | Define qual é o protocolo do sistema de mensageria que utilizado pela rede para notificar o estado das transações. | tcp                                                                                                       |
| ZMQ_SOCKET_URL | Define qual é a URL do sistema de mensageria que utilizado pela rede para notificar o estado das transações. | zmq.devnet.iota.org                                                                                                       |
| ZMQ_SOCKET_PORT | Define qual é a porta do sistema de mensageria que utilizado pela rede para notificar o estado das transações. | 5556                                                                                                       |

| :arrow_left: [load-monitor](https://github.com/larsid/soft-iot-dlt-load-monitor#readme) | ............................... :arrow_up: [Voltar ao topo](#soft-iot-dlt-client-tangle) :arrow_up: ............................... | [load-balancer](https://github.com/larsid/soft-iot-dlt-client-tangle#readme) :arrow_right: |
| :-------------------------------------------------------------------------------------: | ----------------------------------------------------------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------: |
