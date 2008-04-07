/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovy.beans.vm5

import org.codehaus.groovy.control.CompilationFailedException

/**
 * @author Danno Ferrin (shemnon)
 */
class BindableTest extends GroovyTestCase {

    public void testSimpleBindableProperty() {
        GroovyShell shell = new GroovyShell()
        shell.evaluate("""
            import groovy.beans.Bindable

            class SimpleBean {
                @Bindable String name
            }

            sb = new SimpleBean()
            sb.name = "bar"
            changed = false
            sb.propertyChange = {changed = true}
            sb.name = "foo"
            assert changed
        """)
    }

    public void testMultipleBindableProperty() {
        GroovyShell shell = new GroovyShell()
        shell.evaluate("""
            import groovy.beans.Bindable

            class SimpleBean {
                @Bindable String name
                @Bindable String value
            }

            sb = new SimpleBean(name:"foo", value:"bar")
            changed = 0
            sb.propertyChange = {changed++}
            sb.name = "baz"
            sb.value = "biff"
            assert changed == 2
        """)
    }

    public void testExisingSetter() {
        GroovyShell shell = new GroovyShell()
        shouldFail(CompilationFailedException) {
            shell.evaluate("""
                class SimpleBean {
                    @groovy.beans.Bindable String name
                    void setName() { }
                }
            """)
        }
    }

    public void testOnField() {
        GroovyShell shell = new GroovyShell()
        shouldFail(CompilationFailedException) {
            shell.evaluate("""
                class SimpleBean {
                    public @groovy.beans.Bindable String name
                    void setName() { }
                }
            """)
        }
    }

    public void testOnClass() {
        GroovyShell shell = new GroovyShell()
        shell.evaluate("""
            import groovy.beans.Bindable

            @Bindable
            class SimpleBean {
                String name
                String value
            }

            sb = new SimpleBean(name:"foo", value:"bar")
            changed = 0
            sb.propertyChange = {changed++}
            sb.name = "baz"
            sb.value = "biff"
            assert changed == 2
        """)

        shell = new GroovyShell()
        shell.evaluate("""
            import groovy.beans.Bindable

            @Bindable
            class SimpleBean {
                String name
                @Bindable String value
            }

            sb = new SimpleBean(name:"foo", value:"bar")
            changed = 0
            sb.propertyChange = {changed++}
            sb.name = "baz"
            sb.value = "biff"
            assert changed == 2
        """)

        shell = new GroovyShell()
        shell.evaluate("""
            import groovy.beans.Bindable

            @Bindable
            class SimpleBean {
                @Bindable String name
                @Bindable String value
            }

            sb = new SimpleBean(name:"foo", value:"bar")
            changed = 0
            sb.propertyChange = {changed++}
            sb.name = "baz"
            sb.value = "biff"
            assert changed == 2
        """)

    }
}