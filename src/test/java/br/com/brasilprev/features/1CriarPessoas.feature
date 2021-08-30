Feature: Criar uma pessoa no serviço
  Scenario: Efetuar a inclusão de uma pessoa no servço
    Given serviço esteja no ar
    When incluir pessoa
    Then verificar se uma pessoa foi incluída "Jeremias Santos da Silva"

  Scenario: incluir duas pessoas com o mesmo CPF
    Given serviço esteja no ar
    When incluir pessoa com CPF cadastrado "32541221542"
    Then validar se a pessoa não foi incluida 400 "erro"

  Scenario: incluir duas pessoas com o mesmo ddd e numero de telefone
    Given serviço esteja no ar
    When incluir uma pessoa com ddd e telefone cadastrados "61", "981007641"
    Then validar se a pessoa não foi incluida 400 "erro"
