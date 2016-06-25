-- :name all-bindings :? :*
-- :doc get all bindings
SELECT * FROM bindings

-- :name add-binding :! :n
-- :doc add new binding
INSERT INTO bindings
(binding, command, description, times_used)
VALUES (:binding, :command, :description, 0)

-- :name delete-binding :! :n
-- :doc delete binding with given id
DELETE FROM bindings
WHERE id = :id

-- :name increase-usage :! :n
-- :doc increase times used for binding
UPDATE bindings
SET times_used = times_used + 1
WHERE id = :id
